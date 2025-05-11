import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import {Message} from '../../core/model/MessageModel';
import { MessageStatusUpdate } from '../../core/model/MessageStatusUpdate';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  private stompClient: any;
  private readonly messageSubject = new Subject<Message>();
  private readonly statusSubject = new Subject<MessageStatusUpdate>();
  private username: string = '';

  constructor() {
    this.username = localStorage.getItem('username') ?? '';
    this.connect();
  }

  private connect() {
    const socket = new SockJS('http://localhost:8080/ws');
    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({}, () => {
      if (this.username && this.username.trim() !== ''){
        this.setUsername(this.username);
      }
    });
  }

  setUsername(username: string) {
    this.username = username;
    this.stompClient.subscribe('/queue/messages-' + username, (message: any) => {
      const msg: Message = JSON.parse(message.body);
      this.messageSubject.next(msg);
      // Send DELIVERED acknowledgment
      if (msg.id) {
        this.sendStatusUpdate(msg.id, 'DELIVERED');
      }
    });
    // Subscribe to status updates
    this.stompClient.subscribe('/queue/status-' + username, (status: any) => {
      this.statusSubject.next(JSON.parse(status.body));
    });
  }

  sendMessage(message:Message ) {
    this.stompClient.send('/app/send', {}, JSON.stringify(message));
  }

  // Send status update (DELIVERED or READ)
  sendStatusUpdate(messageId: number, status: string) {
    const statusUpdate: MessageStatusUpdate = { messageId, status };
    this.stompClient.send(`/app/${status.toLowerCase()}`, {}, JSON.stringify(statusUpdate));
  }

  getMessages() {
    return this.messageSubject.asObservable();
  }

  getStatusUpdates() {
    return this.statusSubject.asObservable();
  }
}
