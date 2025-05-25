import { Component, OnInit } from '@angular/core';
import { PageResponseBookResponse as PageResponseFavoriteBookResponse} from '../../../../services/models/page-response-book-response';
import { BookService } from '../../../../services/services/book.service';
import { Router } from '@angular/router';
import { BookResponse } from '../../../../services/models/book-response';

@Component({
  selector: 'app-favorites',
  templateUrl: './favorites.component.html',
  styleUrl: './favorites.component.scss'
})
export class FavoritesComponent implements OnInit{
  bookResponse: PageResponseFavoriteBookResponse = {};
  page = 0;
  size = 4;
  message = '';
  level = 'success';
  

  constructor(
    private bookService: BookService,
    private router: Router,
  ){}

  ngOnInit(): void {
    this.findFavouriteBooksByUser();
  }
  
  private findFavouriteBooksByUser() {
    this.bookService.findFavouriteBooksByUser({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (response: PageResponseFavoriteBookResponse) => {
        this.bookResponse = response;
      },
      error: (err) => {
        console.error('Error fetching favourite books:', err);
        // Handle error appropriately, e.g., show a notification or redirect
      }
    });
  }

  goToFirstPage(){
    this.page = 0;
    this.findFavouriteBooksByUser();
  }

  goToPreviousPage() {
    this.page--;
    this.findFavouriteBooksByUser();
  }

  goToPage(page: number) {
    this.page = page;
    this.findFavouriteBooksByUser();
  }

  goToNextPage() {
    this.page++;
    this.findFavouriteBooksByUser();
  }

  goToLastPage() {
    this.page = this.bookResponse.totalPages as number - 1;
    this.findFavouriteBooksByUser();
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
}
