import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import {Message} from '../../core/model/MessageModel';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  private stompClient: any;
  private readonly messageSubject = new Subject<Message>();
  private username: string = '';

  constructor() {
    this.username = localStorage.getItem('username') ?? '';
    this.connect();
  }

  private connect() {
    const socket = new SockJS('http://localhost:8080/ws');
    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({}, (frame: any) => {
      if (this.username && this.username.trim() !== ''){
        this.setUsername(this.username);
      }
    });
  }

  setUsername(username: string) {
    this.username = username;
    this.stompClient.subscribe('/queue/messages-' + username, (message: any) => {
      this.messageSubject.next(JSON.parse(message.body));
    });
  }

  sendMessage(to: string, content: string) {
    const message: Message = {
      sender: this.username,
      recipient: to,
      content: content
    };
    this.stompClient.send('/app/send', {}, JSON.stringify(message));
  }

  getMessages() {
    return this.messageSubject.asObservable();
  }
}
