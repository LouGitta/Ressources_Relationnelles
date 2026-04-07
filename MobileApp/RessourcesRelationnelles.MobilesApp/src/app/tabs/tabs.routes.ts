import { Routes } from '@angular/router';
import { TabsPage } from './tabs.page';

export const routes: Routes = [
  {
    path: 'tabs',
    component: TabsPage,
    children: [
      {
        path: 'friends',
        loadComponent: () =>
          import('../friends/friends.page').then((m) => m.FriendsPage),
      },
      {
        path: 'ressources',
        loadComponent: () =>
          import('../ressources/ressources.page').then((m) => m.RessourcesPage),
      },
      {
        path: 'ressources/:id',
        loadComponent: () =>
          import('../ressource-details/ressource-details.page').then(
            (m) => m.RessourceDetailsPage,
          ),
      },
      {
        path: 'account',
        loadComponent: () =>
          import('../account/account.page').then((m) => m.AccountPage),
      },
      {
        path: '',
        redirectTo: '/tabs/ressources',
        pathMatch: 'full',
      },
    ],
  },
  {
    path: '',
    redirectTo: '/tabs/ressources',
    pathMatch: 'full',
  },
];
