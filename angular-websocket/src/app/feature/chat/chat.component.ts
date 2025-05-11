import { Component, OnInit } from '@angular/core';
import { WebsocketService } from '../../service/websocket/websocket.service';
import { FormsModule } from '@angular/forms';
import {DatePipe, NgClass, NgForOf, NgIf} from '@angular/common';
import { UserModel } from '../../core/model/UserModel';
import { UserService } from '../../service/user/user.service';
import { MessageService } from '../../service/message/message.service';
import {Message} from '../../core/model/MessageModel';
import {MessageStatusUpdate} from '../../core/model/MessageStatusUpdate'; // Import new service

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [FormsModule, NgForOf, NgClass, DatePipe, NgIf],
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

    this.websocketService.getStatusUpdates().subscribe((statusUpdate: MessageStatusUpdate) => {
      console.log('Status update received:', statusUpdate);
      const message = this.messages.find(m => m.id === statusUpdate.messageId);
      if (message) {
        message.status = statusUpdate.status;
      }
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
      const sendingMessage: Message = {
        sender: this.username,
        recipient: this.recipient,
        content: this.message,
        timestamp: new Date().toISOString(),
        status: 'SENT'
      };
      this.websocketService.sendMessage(sendingMessage);
      this.message = '';
    }
  }

  async selectUser(username: string) {
    this.recipient = username;
    this.messages = [];
    await this.fetchChatHistory();
    this.markMessagesAsRead();
  }

  private fetchChatHistory(): Promise<void> {
    return new Promise((resolve, reject) => {
      if (this.recipient) {
        this.messageService.getChatHistory(this.username, this.recipient).subscribe({
          next: (res: any) => {
            if (res.data) {
              this.messages = res.data;
            }
            resolve();
          },
          error: (err: any) => {
            reject(new Error(err));
          }
        });
      } else {
        resolve();
      }
    });
  }

  private markMessagesAsRead() {
    this.messages
      .filter(msg => msg.sender === this.recipient && msg.status !== 'READ')
      .forEach(msg => {
        if (msg.id) {
          this.websocketService.sendStatusUpdate(msg.id, 'READ');
          msg.status = 'READ';
        }
      });
  }
}
