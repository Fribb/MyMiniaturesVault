import { Routes } from '@angular/router';
import {CreatorComponent} from './routes/creator/creator.component';
import {BundleComponent} from './routes/bundle/bundle.component';

export const routes: Routes = [
  { path: 'creator', component: CreatorComponent },
  { path: 'bundle', component: BundleComponent },
];
