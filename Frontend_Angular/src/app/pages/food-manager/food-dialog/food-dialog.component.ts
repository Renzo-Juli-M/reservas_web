import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatToolbarModule } from '@angular/material/toolbar';
import { FoodDTO, FoodService } from '../../../services/food/food.service';

@Component({
  selector: 'app-food-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatInputModule,
    MatButtonModule,
    MatSlideToggleModule,
    MatToolbarModule
  ],
  templateUrl: './food-dialog.component.html',
  styleUrl: './food-dialog.component.css'
})
export class FoodDialogComponent {
  food: Omit<FoodDTO, 'idFood'> = {
    name: '',
    price: 0,
    available: true
  };

  constructor(
    private dialogRef: MatDialogRef<FoodDialogComponent>,
    private foodService: FoodService
  ) {}

  close(): void {
    this.dialogRef.close();
  }

  isFormValid(): boolean {
    return !!this.food.name && this.food.price > 0;
  }

  create(): void {
    if (!this.isFormValid()) return;

    this.foodService.save(this.food).subscribe({
      next: () => this.dialogRef.close(this.food),
      error: (err) => console.error('Error creando alimento', err)
    });
  }
}
