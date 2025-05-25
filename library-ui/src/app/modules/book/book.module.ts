import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { BookRoutingModule } from './book-routing.module';
import { MainComponent } from './pages/main/main.component';
import { MenuComponent } from './components/menu/menu.component';
import { BookListComponent } from './pages/book-list/book-list.component';
import { BookCardComponent } from './components/book-card/book-card.component';
import { FavoritesComponent } from './pages/favorites/favorites.component';
import { ManageBookComponent } from './pages/manage-book/manage-book.component';
import {FormsModule} from "@angular/forms";
import { BorrowedBookListComponent } from './pages/borrowed-book-list/borrowed-book-list.component';


@NgModule({
  declarations: [
    MainComponent,
    MenuComponent,
    BookListComponent,
    BookCardComponent,
    FavoritesComponent,
    ManageBookComponent,
    BorrowedBookListComponent
  ],
    imports: [
        CommonModule,
        BookRoutingModule,
        FormsModule
    ]
})
export class BookModule { }
