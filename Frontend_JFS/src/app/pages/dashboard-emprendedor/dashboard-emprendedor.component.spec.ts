import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardEmprendedorComponent } from './dashboard-emprendedor.component';

describe('DashboardEmprendedorComponent', () => {
  let component: DashboardEmprendedorComponent;
  let fixture: ComponentFixture<DashboardEmprendedorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardEmprendedorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashboardEmprendedorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
