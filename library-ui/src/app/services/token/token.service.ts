import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  private jwtHelper = new JwtHelperService();


  isTokenNotValid() {
    return !this.isTokenValid();
  }

  private isTokenValid() {
    const token = this.token;
    if(!token){
      return false;
    }
    //decode the token
    const jwtHelper = new JwtHelperService();
    // check expiration date
    const isTokenExpired = jwtHelper.isTokenExpired(token);
    if(isTokenExpired) {
      localStorage.clear();
      return false;
    }
    return true;
  }

  set token(token: string){
    localStorage.setItem('token', token);
  }

  get token(){
    return localStorage.getItem('token') as string;
  }

    public getUserRoles(): string[] {
    const token = this.token;
    // Ensure token exists and is valid before attempting to decode
    if (token && this.isTokenValid()) {
      try {
        const decodedToken = this.jwtHelper.decodeToken(token);
        // Based on your JwtService.java, roles are in the 'authorities' claim
        if (decodedToken && decodedToken.authorities && Array.isArray(decodedToken.authorities)) {
          return decodedToken.authorities;
        }
        return [];
      } catch (error) {
        console.error('Error decoding token:', error);
        return [];
      }
    }
    return [];
  }

}
