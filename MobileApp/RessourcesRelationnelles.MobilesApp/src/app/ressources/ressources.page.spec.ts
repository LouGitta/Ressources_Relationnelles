import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RessourcesPage } from './ressources.page';

describe('Tab2Page', () => {
  let component: RessourcesPage;
  let fixture: ComponentFixture<RessourcesPage>;

  beforeEach(async () => {
    fixture = TestBed.createComponent(RessourcesPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
