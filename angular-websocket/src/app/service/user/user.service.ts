import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {UserModel} from '../../core/model/UserModel';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private readonly http:HttpClient) { }

  getUsers(username:string){
    return this.http.post('http://localhost:8080/users/user/join',username);
  }

  register(user: UserModel) {
    return this.http.post('http://localhost:8080/users/register',user)
  }

  getAllUsers(){
    return this.http.get('http://localhost:8080/users/all');
  }

  getAllOtherUsers(username: string){
    return this.http.get(`http://localhost:8080/users/all/except/${username}`);
  }
}
