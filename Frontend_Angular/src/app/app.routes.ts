import { Routes } from '@angular/router';
import { RoomManagerComponent } from './pages/room-manager/room-manager.component';
import { ReservationManagerComponent } from './pages/reservation-manager/reservation-manager.component';
import {ChatbotComponent} from './components/chatbot/chatbot.component';
import {DashboardEmprendedorComponent} from './pages/dashboard-emprendedor/dashboard-emprendedor.component';

export const routes: Routes = [
  { path: 'room-manager', component: RoomManagerComponent },
  { path: 'reservation-manager', component: ReservationManagerComponent },
  { path: 'chatbot', component: ChatbotComponent },
  { path: 'dashboard-emprendedor', component: DashboardEmprendedorComponent }

];
