import { Component } from '@angular/core';

import {CommonModule, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {UserService} from '../../service/user/user.service';
import {Router, RouterLink} from '@angular/router';
import {WebsocketService} from '../../service/websocket/websocket.service';

@Component({
  selector: 'app-join',
  standalone: true,
  imports: [FormsModule, CommonModule, NgIf, RouterLink],
  templateUrl: './join.component.html',
  styleUrl: './join.component.css'
})
export class JoinComponent {
  username:string = '';
  constructor(private readonly userService:UserService,private readonly router:Router,private readonly websocketService : WebsocketService) {}

  onSubmit(){
    if (this.username!= '' && this.username){
      this.userService.getUsers(this.username).subscribe({
        next:() =>{
          this.websocketService.setUsername(this.username);
          localStorage.setItem('username',this.username);
          this.router.navigate(['/chat']);
        },
        error:(err)=>{
          console.log(err)
        }
      })
    }
  }
}
