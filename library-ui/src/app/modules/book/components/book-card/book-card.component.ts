import { Component, EventEmitter, Input, Output } from '@angular/core';
import { BookResponse } from '../../../../services/models';

@Component({
  selector: 'app-book-card',
  templateUrl: './book-card.component.html',
  styleUrl: './book-card.component.scss'
})
export class BookCardComponent {
  private _book: BookResponse = {};
  private _manage: boolean = false;
  private _bookCover: string | undefined;

  get book(): BookResponse {
    return this._book;
  }

  @Input()
  set book(value: BookResponse) {
    this._book = value;
  }

  get manage(): boolean {
    return this._manage;
  }

  @Input()
  set manage(value: boolean){
    this._manage = value;
  }

  get bookCover(): string | undefined {
    if(this._book.cover){
      return 'data:image/jpg;base64,' + this._book.cover;
    }
    return 'https://www.au-ibar.org/sites/default/files/default_images/no-cover.jpg';
  }

  get authorDisplayNames(): string {
    if (!this.book || !this.book.authors || this.book.authors.length === 0) {
      return '';
    }
    return this.book.authors
      .map((author: any) => `${author.firstName || ''} ${author.lastName || ''}`.trim())
      .filter((name: string) => name.length > 0)
      .join(', ');
  }

  @Output() private details: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private borrow: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private addToFavourites: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private edit: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private share: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private delete: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  

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
    this.delete.emit(this._book);
  }
}
