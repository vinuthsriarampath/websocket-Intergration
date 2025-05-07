import { Component } from '@angular/core';
import {Message, WebsocketService} from '../service/websocket/websocket.service';
import {FormsModule} from '@angular/forms';
import {NgForOf} from '@angular/common';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [FormsModule, NgForOf],
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.css'
})
export class ChatComponent {
  username: string = '';
  recipient: string = '';
  message: string = '';
  messages: Message[] = [];

  constructor(private websocketService: WebsocketService) {}

  ngOnInit() {
    this.websocketService.getMessages().subscribe((msg: Message) => {
      this.messages.push(msg);
    });
  }

  setUsername() {
    this.websocketService.setUsername(this.username);
  }

  sendMessage() {
    this.websocketService.sendMessage(this.recipient, this.message);
    this.message = '';
  }
}
