import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

// Angular Material Imports
import { MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { RoomDTO, RoomsService } from '../../../services/room/rooms.service';


@Component({
  selector: 'app-room-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatToolbarModule,
    MatSlideToggleModule
  ],
  templateUrl: './room-dialog.component.html',
  styleUrl: './room-dialog.component.css'
})
export class RoomDialogComponent {
  room: RoomDTO = {
    number: '',
    type: '',
    price: 0,
    available: true,
    idRoom: null
  };

  roomTypes: string[] = ['INDIVIDUAL', 'DOBLE', 'SUITE'];

  constructor(
    private _dialogRef: MatDialogRef<RoomDialogComponent>,
    private roomsService: RoomsService
  ) {}

  close() {
    this._dialogRef.close();
  }

  isFormValid(): boolean {
    return !!this.room.number &&
           !!this.room.type &&
           this.room.price > 0;
  }

  create() {
    if (!this.isFormValid()) {
      return;
    }

    this.roomsService.save(this.room).subscribe({
      next: () => {
        this._dialogRef.close(this.room);
      },
      error: (error) => {
        console.error('Error creando habitación', error);
      }
    });
  }
}
