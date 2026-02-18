import { Component } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  template: `
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
      <div class="container">
        <a class="navbar-brand" routerLink="/admin">
          <i class="bi bi-bank2"></i> <strong>BANCO LEONCIO</strong>
        </a>

      </div>
    </nav>

    <div class="container mt-4">
      <router-outlet></router-outlet>
    </div>
  `,
  styles: [`
    .active { font-weight: bold; color: #fff !important; }
  `]
})
export class AppComponent { }
