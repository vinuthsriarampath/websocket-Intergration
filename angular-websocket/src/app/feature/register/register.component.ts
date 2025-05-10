import { Component } from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {UserService} from '../../service/user/user.service';
import {Router} from '@angular/router';
import {UserModel} from '../../core/model/UserModel';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule,
    CommonModule
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  user:UserModel = {
    username: '',
    email: ''
  }

  constructor(private readonly userService:UserService,private readonly router:Router) {}

  onSubmit(){
    console.log('Form data:', this.user);
    this.userService.register(this.user).subscribe({
      next: () => {
        console.log('User registered successfully');
        this.router.navigate(['']);
      },
      error: (err) => {
        console.error('Error registering user:', err);
      }
    });
  }

}
