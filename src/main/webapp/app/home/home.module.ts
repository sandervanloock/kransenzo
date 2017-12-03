import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {KransenzoSharedModule} from '../shared';

import {HOME_ROUTE, HomeComponent} from './';
import {HomeProductComponent} from './home-product.component';

@NgModule({
    imports: [
        KransenzoSharedModule,
        RouterModule.forChild([ HOME_ROUTE ])
    ],
    declarations: [
        HomeComponent, HomeProductComponent
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KransenzoHomeModule {}
