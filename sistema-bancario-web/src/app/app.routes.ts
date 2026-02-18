import { Routes } from '@angular/router';
// Ajuste os nomes para bater com seus arquivos reais:
import { AdminComponent } from './components/admin/admin';
import { UsuarioComponent } from './components/usuario/usuario';
import { HomeComponent } from './components/home/home';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'admin', component: AdminComponent },
  { path: 'usuario/:id', component: UsuarioComponent },
  { path: '', redirectTo: '/admin', pathMatch: 'full' }
];
