import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MobilityDTO, MobilityService } from '../../../services/mobility/mobility.service';

@Component({
  selector: 'app-mobility-dialog',
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
  templateUrl: './mobility-dialog.component.html',
  styleUrls: ['./mobility-dialog.component.css']
})
export class MobilityDialogComponent {
  // 🔧 Eliminamos idMobility para que no se envíe al backend
  mobility: Omit<MobilityDTO, 'idMobility'> = {
    plate: '',
    type: 'CAR',
    capacity: 0,
    available: true
  };

  mobilityTypes: string[] = ['CAR', 'BUS'];

  constructor(
    private dialogRef: MatDialogRef<MobilityDialogComponent>,
    private mobilityService: MobilityService
  ) {}

  close(): void {
    this.dialogRef.close();
  }

  isFormValid(): boolean {
    return !!this.mobility.plate && !!this.mobility.type && this.mobility.capacity > 0;
  }

  create(): void {
    if (!this.isFormValid()) return;

    this.mobilityService.save(this.mobility).subscribe({
      next: () => this.dialogRef.close(this.mobility),
      error: err => {
        console.error('Error creando movilidad', err);
        alert('❌ Error creando movilidad: ' + err.message);
      }
    });
  }
}
