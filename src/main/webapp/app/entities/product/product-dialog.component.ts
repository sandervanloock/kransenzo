import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Product } from './product.model';
import { ProductPopupService } from './product-popup.service';
import { ProductService } from './product.service';
import { Tag, TagService } from '../tag';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-product-dialog',
    templateUrl: './product-dialog.component.html'
})
export class ProductDialogComponent implements OnInit {

    product: Product;
    isSaving: boolean;

    tags: Tag[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private productService: ProductService,
        private tagService: TagService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.tagService.query()
            .subscribe((res: ResponseWrapper) => { this.tags = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.product.id !== undefined) {
            this.subscribeToSaveResponse(
                this.productService.update(this.product));
        } else {
            this.subscribeToSaveResponse(
                this.productService.create(this.product));
        }
    }

    private subscribeToSaveResponse(result: Observable<Product>) {
        result.subscribe((res: Product) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Product) {
        this.eventManager.broadcast({ name: 'productListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackTagById(index: number, item: Tag) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-product-popup',
    template: ''
})
export class ProductPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private productPopupService: ProductPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.productPopupService
                    .open(ProductDialogComponent as Component, params['id']);
            } else {
                this.productPopupService
                    .open(ProductDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
