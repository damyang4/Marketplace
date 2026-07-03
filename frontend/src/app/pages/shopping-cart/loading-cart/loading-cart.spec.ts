import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoadingCart } from './loading-cart';

describe('LoadingCart', () => {
  let component: LoadingCart;
  let fixture: ComponentFixture<LoadingCart>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoadingCart],
    }).compileComponents();

    fixture = TestBed.createComponent(LoadingCart);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
