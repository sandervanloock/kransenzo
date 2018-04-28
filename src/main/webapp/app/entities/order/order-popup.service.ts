import {Component, Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {DatePipe} from '@angular/common';
import {Order} from './order.model';
import {OrderService} from './order.service';

@Injectable()
export class OrderPopupService {
    private ngbModalRef: NgbModalRef;

    constructor( private datePipe: DatePipe, private modalService: NgbModal, private router: Router, private orderService: OrderService ) {
        this.ngbModalRef = null;
    }

    open( component: Component, id?: number | any ): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>( ( resolve, reject ) => {
            const isOpen = this.ngbModalRef !== null;
            if ( isOpen ) {
                resolve( this.ngbModalRef );
            }

            if ( id ) {
                this.orderService.find( id ).subscribe( ( order ) => {
                    order.created = this.datePipe
                        .transform( order.created, 'yyyy-MM-ddTHH:mm:ss' );
                    order.updated = this.datePipe
                        .transform( order.updated, 'yyyy-MM-ddTHH:mm:ss' );
                    this.ngbModalRef = this.orderModalRef( component, order );
                    resolve( this.ngbModalRef );
                } );
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout( () => {
                    this.ngbModalRef = this.orderModalRef( component, new Order() );
                    resolve( this.ngbModalRef );
                }, 0 );
            }
        } );
    }

    orderModalRef( component: Component, order: Order ): NgbModalRef {
        const modalRef = this.modalService.open( component, {size: 'lg', backdrop: 'static'} );
        modalRef.componentInstance.order = order;
        modalRef.result.then( ( result ) => {
            this.router.navigate( [{outlets: {popup: null}}], {replaceUrl: true} );
            this.ngbModalRef = null;
        }, ( reason ) => {
            this.router.navigate( [{outlets: {popup: null}}], {replaceUrl: true} );
            this.ngbModalRef = null;
        } );
        return modalRef;
    }
}
