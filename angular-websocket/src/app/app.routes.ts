import { Routes } from '@angular/router';
import {JoinComponent} from './feature/join/join.component';
import {RegisterComponent} from './feature/register/register.component';
import {ChatComponent} from './feature/chat/chat.component';

export const routes: Routes = [
  {
    path:'',
    component: JoinComponent
  },
  {
    path:'register',
    component: RegisterComponent
  },
  {
    path:'chat',
    component: ChatComponent
  }
];
