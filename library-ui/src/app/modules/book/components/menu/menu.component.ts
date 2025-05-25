import { Component, OnInit } from '@angular/core';
import { TokenService } from '../../../../services/token/token.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent implements OnInit {
  constructor(
    private tokenService: TokenService
  ){}

  get userIsAdmin(): boolean {
    const roles = this.tokenService.getUserRoles();
    // Check if 'ADMIN' is one of the roles.
    // This string must exactly match the role name stored in the JWT's 'authorities' claim.
    return roles.includes('ADMIN');
  }

  ngOnInit(): void {
    const linkColor = document.querySelectorAll('.nav-link');
    linkColor.forEach(link => {
      if(window.location.href.endsWith(link.getAttribute('href') || '')){
        link.classList.add('active');
      }
      link.addEventListener('click', () => {
        linkColor.forEach(l => l.classList.remove('active'));
        link.classList.add('active');
      })
    });
  }

  logout(){
    localStorage.removeItem('token');
    window.location.reload();
  }
}
