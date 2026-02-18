import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component'; // Ele importa o arquivo que criamos acima

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
