import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RessourceDetailsPage } from './ressource-details.page';

describe('RessourceDetailsPage', () => {
  let component: RessourceDetailsPage;
  let fixture: ComponentFixture<RessourceDetailsPage>;

  beforeEach(() => {
    fixture = TestBed.createComponent(RessourceDetailsPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
