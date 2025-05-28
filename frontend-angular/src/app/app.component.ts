import {ChangeDetectionStrategy, Component, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {TuiAppearance, TuiButton, TuiHint, TuiRoot} from '@taiga-ui/core';
import {TuiFade} from '@taiga-ui/kit';
import {TuiNavigation} from '@taiga-ui/layout';
import {DarkModeService} from './shared/services/dark-mode.service';
import {RouterLink, RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [FormsModule, TuiButton, TuiNavigation, TuiFade, TuiRoot, TuiHint, TuiAppearance, RouterOutlet, RouterLink],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class AppComponent {
  title = 'frontend-angular';
  protected expanded = signal(false);
  protected open:boolean = false;

  constructor(public readonly darkModeService: DarkModeService) {}

  protected handleToggle(): void {
    this.expanded.update((e) => !e);
  }

  protected toggleDarkMode(): boolean {
    return this.darkModeService.darkMode();
  }
}
