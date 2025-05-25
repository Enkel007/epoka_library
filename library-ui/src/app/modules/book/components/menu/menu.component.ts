import { Component, OnInit } from '@angular/core';
import { TokenService } from '../../../../services/token/token.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent implements OnInit {
  searchQuery: string = '';

  constructor(
    private tokenService: TokenService,
    private router: Router
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

  search(): void {
    // if (this.searchQuery && this.searchQuery.trim() !== '') {
    //   // Navigate to the search results page with the query as a parameter
    //   this.router.navigate(['/books'], { 
    //     queryParams: { 
    //       search: this.searchQuery.trim() 
    //     } 
    //   });
    // }
  }

  logout(){
    localStorage.removeItem('token');
    window.location.reload();
  }
}
