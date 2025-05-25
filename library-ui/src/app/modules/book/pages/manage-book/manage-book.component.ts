import { Component, OnInit } from '@angular/core'; // Import OnInit
import { BookRequest, BookResponse } from '../../../../services/models';
import { BookService } from '../../../../services/services/book.service';
import { ActivatedRoute, Router } from '@angular/router';

// Manually define an array based on your Category enum for use in the template
// This ensures the values match your backend enum exactly.
const categoryValues = [
    'FICTION', 'NON_FICTION', 'SCIENCE_FICTION', 'FANTASY', 'MYSTERY',
    'THRILLER', 'ROMANCE', 'HORROR', 'HISTORICAL_FICTION', 'COMICS',
    'GRAPHIC_NOVEL', 'COMEDY', 'HISTORICAL', 'BIOGRAPHY', 'AUTOBIOGRAPHY',
    'SELF_HELP', 'BUSINESS', 'COOKING', 'ART', 'TRAVEL', 'HISTORY',
    'SCIENCE', 'POETRY', 'DRAMA', 'ACADEMIC', 'REFERENCE', 'CHILDREN',
    'YOUNG_ADULT', 'MATURE_AUDIENCE'
];

@Component({
  selector: 'app-manage-book',
  templateUrl: './manage-book.component.html',
  styleUrl: './manage-book.component.scss'
})
export class ManageBookComponent implements OnInit { // Implement OnInit
  bookRequest: BookRequest = {
    author: '', // Assuming authors will be handled differently, perhaps another input or select
    categories: [], // Initialize as an empty array for multi-select
    description: '',
    isbn: '',
    title: '',
    available: true,
  };
  errorMsg: Array<string> = [];
  selectedBookCover: any;
  selectedPicture: string | undefined;

  // Expose categories to the template
  public allCategories: string[] = categoryValues;

  constructor(
    private bookService: BookService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ){}

  ngOnInit(): void {
    const bookId = this.activatedRoute.snapshot.params['bookId'];
    if(bookId){
      this.bookService.findBookById({
        'book-id': bookId
      }).subscribe({
        next: (book: BookResponse) => {
          this.bookRequest = {
            id: book.id,
            title: book.title as string,
            author: book.author as string,
            isbn: book.isbn as string,
            description: book.description as string,
            categories: book.categories as Array<'FICTION' | 'NON_FICTION' | 'SCIENCE_FICTION' | 'FANTASY' | 'MYSTERY' | 'THRILLER' | 'ROMANCE' | 'HORROR' | 'HISTORICAL_FICTION' | 'COMICS' | 'GRAPHIC_NOVEL' | 'BIOGRAPHY' | 'AUTOBIOGRAPHY' | 'SELF_HELP' | 'BUSINESS' | 'COOKING' | 'ART' | 'TRAVEL' | 'HISTORY' | 'SCIENCE' | 'POETRY' | 'DRAMA' | 'ACADEMIC' | 'REFERENCE' | 'CHILDREN' | 'YOUNG_ADULT' | 'MATURE_AUDIENCE'>,
            available: book.available
          }
          if(book.cover){
            this.selectedPicture = 'data:image/jpg;base64,' + book.cover;
          }
        }
      })
    }
  }

  onFileSelected(event: any) {
    this.selectedBookCover = event.target.files[0];
    console.log(this.selectedBookCover);
    if(this.selectedBookCover) {
      const reader = new FileReader();
      reader.onload = () => {
        this.selectedPicture = reader.result as string;
      }
      reader.readAsDataURL(this.selectedBookCover);
    }
  }

  saveBook(){
    this.bookService.saveBook({
      body: this.bookRequest
    }).subscribe({
      next: (bookId) => {
        // It seems bookId might be a number or not defined in some BookService responses.
        // Ensure 'book-id' key matches what your API expects.
        // If bookId is an object with an id property, use bookId.id
        const id = typeof bookId === 'number' ? bookId : (bookId as any)?.id;
        if (id === undefined) {
            console.error('Book ID is undefined after saving.');
            this.errorMsg = ['Failed to get book ID after saving. Cannot upload cover.'];
            return;
        }
        if (this.selectedBookCover) {
            this.bookService.uploadBookCoverPicture({
              'book-id': id,
              body: {
                file: this.selectedBookCover
              }
            }).subscribe({
              next: () => {
                this.router.navigate(['/books']);
              },
              error: (err) => {
                // It's good to give feedback about cover upload failure too
                console.error('Error uploading book cover:', err);
                this.errorMsg.push('Book saved, but cover upload failed.');
                // Optionally navigate or stay on page
                 this.router.navigate(['/books']); // Or handle as per your UX
              }
            });
        } else {
            this.router.navigate(['/books']);
        }
      },
      error: (err) => {
        if (err.error && err.error.validationErrors) {
          this.errorMsg = err.error.validationErrors;
        } else if (err.error && err.error.error) {
          this.errorMsg = [err.error.error];
        } 
         else {
          this.errorMsg = ['An unexpected error occurred.'];
          console.error(err);
        }
      }
    });
  }
}