import {ChangeDetectionStrategy, Component, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {TuiButton, TuiDataList, TuiIcon, TuiLabel, TuiTitle} from '@taiga-ui/core';
import {TuiBadgeNotification, TuiFade, TuiSwitch} from '@taiga-ui/kit';
import {TuiNavigation} from '@taiga-ui/layout';

@Component({
  selector: 'app-root',
  imports: [FormsModule, TuiButton, TuiDataList, TuiNavigation, TuiSwitch, TuiIcon, TuiFade, TuiBadgeNotification, TuiLabel, TuiTitle],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class AppComponent {
  title = 'frontend-angular';
  protected expanded = signal(false);
  protected open:boolean = false;
  protected switch:boolean = false;

  protected handleToggle(): void {
    this.expanded.update((e) => !e);
  }
}
