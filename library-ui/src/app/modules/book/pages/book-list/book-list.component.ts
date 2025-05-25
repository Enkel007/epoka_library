import { Component, OnInit } from '@angular/core';
import { BookService } from '../../../../services/services/book.service';
import { Router } from '@angular/router';
import { BookResponse, PageResponseBookResponse } from '../../../../services/models';
import { TokenService } from '../../../../services/token/token.service';

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.scss'
})
export class BookListComponent implements OnInit {
  bookResponse: PageResponseBookResponse = {};
  page = 0;
  size = 4;
  message = '';
  level = 'success';
  

  constructor(
    private bookService: BookService,
    private router: Router,
    private tokenService: TokenService
  ){}

  get userIsAdmin(): boolean {
    const roles = this.tokenService.getUserRoles();
    // Check if 'ADMIN' is one of the roles.
    // This string must exactly match the role name stored in the JWT's 'authorities' claim.
    return roles.includes('ADMIN');
  }

  ngOnInit(): void {
    this.findAllBooks();
  }
  
  private findAllBooks() {
    this.bookService.findAllBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (books) => {
        this.bookResponse = books;
      },
      error: (err) => {
        console.error('Error fetching books:', err);
        // Handle error appropriately, e.g., show a notification or redirect
      }
    });
  }

  goToFirstPage(){
    this.page = 0;
    this.findAllBooks();
  }

  goToPreviousPage() {
    this.page--;
    this.findAllBooks();
  }

  goToPage(page: number) {
    this.page = page;
    this.findAllBooks();
  }

  goToNextPage() {
    this.page++;
    this.findAllBooks();
  }

  goToLastPage() {
    this.page = this.bookResponse.totalPages as number - 1;
    this.findAllBooks();
  }

  get isLastPage(): boolean {
    return this.page == this.bookResponse.totalPages as number - 1;
  }

  borrowBook(book: BookResponse){
    this.message = '';
    this.bookService.borrowBook({
      'book-id': book.id as number
    }).subscribe({
      next: () => {
        this.level = 'success';
        this.message = 'Book borrowed successfully!';
      },
      error: (err) :void => {
        console.log(err);
        this.level = 'error';
        this.message = err.error.error;
      } 
    });
  }

  addToFavourites(book: BookResponse) {
  this.message = '';
  this.bookService.addBookToFavourites({
    'book-id': book.id as number
  }).subscribe({
    next: () => {
      this.level = 'success';
      this.message = 'Book added to favorites successfully!';
      // Refresh the favorites list
    },
    error: (err): void => {
      console.log(err);
      this.level = 'error';
      this.message = err.error.error || 'Failed to add book to favorites';
    }
  });
}

  editBook(book: BookResponse) {
    this.router.navigate(['books', 'manage', book.id]);
  }

  // deleteBook(book: BookResponse) {
  //  this.bookService.deleteBook({

  //  })
  // }
}
