import { Component, OnInit } from '@angular/core'; // Import OnInit
import { BookRequest } from '../../../../services/models';
import { BookService } from '../../../../services/services/book.service';
import { Router } from '@angular/router';

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
    // available: true, // You might want to initialize 'available' if it's part of BookRequest
  };
  errorMsg: Array<string> = [];
  selectedBookCover: any;
  selectedPicture: string | undefined;

  // Expose categories to the template
  public allCategories: string[] = categoryValues;

  constructor(
    private bookService: BookService,
    private router: Router
  ){}

  ngOnInit(): void {
    // If you are editing a book, you would fetch the book data here
    // and populate bookRequest, including bookRequest.categories.
    // For a new book, categories will be an empty array.
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
    // Ensure categories are correctly formatted if your backend expects a Set or specific type
    // For now, it's an array of strings, which should be compatible with Set<Category> on the backend.
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