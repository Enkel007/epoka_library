import { Component, EventEmitter, Input, Output } from '@angular/core';
import { BookResponse } from '../../../../services/models';
import { AuthenticationService } from '../../../../services/services';
import { TokenService } from '../../../../services/token/token.service';

@Component({
  selector: 'app-book-card',
  templateUrl: './book-card.component.html',
  styleUrl: './book-card.component.scss'
})
export class BookCardComponent {
  private _book: BookResponse = {};
  private _bookCover: string | undefined;

  constructor(
    private tokenService: TokenService
  ){}

  get book(): BookResponse {
    return this._book;
  }

  get userIsAdmin(): boolean {
    const roles = this.tokenService.getUserRoles();
    // Check if 'ADMIN' is one of the roles.
    // This string must exactly match the role name stored in the JWT's 'authorities' claim.
    return roles.includes('ADMIN');

  }

  @Input()
  set book(value: BookResponse) {
    this._book = value;
  }

  get bookCover(): string | undefined {
    if(this._book.cover){
      return 'data:image/jpg;base64,' + this._book.cover;
    }
    return 'https://www.au-ibar.org/sites/default/files/default_images/no-cover.jpg';
  }
  
  get categoryDisplayNames(): string{
    if(!this.book || !this.book.categories || this.book.categories.length == 0){
      return '';
    }
    return (this.book.categories as string[])
      .filter((categoryName: string) => categoryName && categoryName.length > 0)
      .join(' ');
  }

  @Output() private details: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private borrow: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private addToFavourites: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private edit: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private share: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private delete: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private categoryClick: EventEmitter<string> = new EventEmitter<string>();
  

  onShowDetails() {
    this.details.emit(this._book);
  }

  onBorrow() {
    this.borrow.emit(this._book);
  }

  onAddToFavourites() {
    this.addToFavourites.emit(this._book);
  }

  onEdit() {
    this.edit.emit(this._book);
  }

  onAvailable() {
    this.share.emit(this._book);
  }

onDelete() {
    // Show a confirmation dialog
    const confirmation = confirm(`Are you sure you want to delete the book "${this._book.title}"?`);
    if (confirmation) {
      // If user confirms, emit the delete event
      this.delete.emit(this._book);
    }
    // If the user cancels, nothing happens
  }

  

  // onCategoryClicked() {
  //   this.categoryClick.emit();
  // }
}
