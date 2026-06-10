import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { RessourceService } from './ressource-service';

describe('RessourceService', () => {
  let service: RessourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(RessourceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
