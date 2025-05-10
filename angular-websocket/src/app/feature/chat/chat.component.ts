import { Component, OnInit } from '@angular/core';
import { WebsocketService } from '../../service/websocket/websocket.service';
import { FormsModule } from '@angular/forms';
import {DatePipe, NgClass, NgForOf} from '@angular/common';
import { UserModel } from '../../core/model/UserModel';
import { UserService } from '../../service/user/user.service';
import { MessageService } from '../../service/message/message.service';
import {Message} from '../../core/model/MessageModel'; // Import new service

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [FormsModule, NgForOf, NgClass, DatePipe],
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.css'
})
export class ChatComponent implements OnInit {
  username: string = '';
  recipient: string = '';
  message: string = '';
  messages: Message[] = [];
  otherUsers: UserModel[] = [];

  constructor(
    private readonly websocketService: WebsocketService,
    private readonly userService: UserService,
    private readonly messageService: MessageService
  ) {
    this.username = localStorage.getItem('username') ?? '';
  }

  ngOnInit() {
    this.websocketService.getMessages().subscribe((msg: Message) => {
      this.messages.push(msg);
    });
    this.fetchAllOtherUsers();
  }

  private fetchAllOtherUsers() {
    this.userService.getAllOtherUsers(this.username).subscribe({
      next: (res: any) => {
        if (res.data) {
          this.otherUsers = res.data;
        }
      },
      error: (err: any) => {
        console.error('Error fetching users:', err);
      }
    });
  }

  sendMessage() {
    if (this.message.trim() && this.recipient) {
      this.websocketService.sendMessage(this.recipient, this.message);
      this.messages.push({
        sender: this.username,
        recipient: this.recipient,
        content: this.message,
        timestamp: new Date().toISOString()
      });
      this.message = '';
    }
  }

  selectUser(username: string) {
    this.recipient = username;
    this.messages = [];
    this.fetchChatHistory();
  }

  private fetchChatHistory() {
    if (this.recipient) {
      this.messageService.getChatHistory(this.username, this.recipient).subscribe({
        next: (res: any) => {
          if (res.data) {
            this.messages = res.data;
          }
        },
        error: (err: any) => {
          console.error('Error fetching chat history:', err);
        }
      });
    }
  }
}
