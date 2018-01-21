import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Customer} from '../entities/customer';

declare var google: any;
declare var $: any;

@Component( {
                selector: 'jhi-customer-address', templateUrl: './customer-address.component.html', styleUrls: ['customer-address.css'],
            } )
export class CustomerAddressComponent implements OnInit {

    @Input() customer: Customer;
    @Output() updateDeliveryPrice: EventEmitter<any> = new EventEmitter();

    private searchBox: any;

    constructor() {
    }

    ngOnInit() {
        const input = document.getElementById( 'address' );
        this.searchBox = new google.maps.places.Autocomplete( input );
        this.searchBox.addListener( 'place_changed', () => this.updateAddress() );
    }

    private updateAddress() {
        const place = this.searchBox.getPlace();
        if ( place != null ) {
            //as defined in https://developers.google.com/maps/documentation/geocoding/intro#ReverseGeocoding
            this.customer.street = place.address_components.filter( attr => attr.types.filter( type => type === 'street_number' || type === 'route' ).length > 0 ).map(
                attr => attr.long_name ).reverse().join( ' ' );
            this.customer.city = place.address_components.filter( attr => attr.types.filter( type => type === 'locality' ).length > 0 ).map( attr => attr.long_name ).join();
            this.customer.province =
                place.address_components.filter( attr => attr.types.filter( type => type === 'administrative_area_level_2' ).length > 0 ).map( attr => attr.long_name ).join();
            this.customer.zipCode = place.address_components.filter( attr => attr.types.filter( type => type === 'postal_code' ).length > 0 ).map( attr => attr.long_name ).join();
            this.customer.description = place.formatted_address;
            if ( place.geometry && place.geometry.location ) {
                this.setLocationAndDeliveryPrice( place );

            }
        }
    }

    private setLocationAndDeliveryPrice( place: any ) {
        this.customer.latitude = place.geometry.location.lat();
        this.customer.longitude = place.geometry.location.lng();
        const directionsService = new google.maps.DirectionsService;
        directionsService.route( {
                                     origin: {lat: 51.055410, lng: 4.490185},
                                     destination: {lat: this.customer.latitude, lng: this.customer.longitude},
                                     waypoints: [],
                                     optimizeWaypoints: true,
                                     travelMode: 'DRIVING'
                                 }, ( response, status ) => {
            if ( status === 'OK' ) {
                if ( response.routes.length && response.routes[0].legs.length ) {
                    const price = Math.round( (response.routes[0].legs[0].distance.value * 0.001) * 100 ) / 100;
                    this.updateDeliveryPrice.emit( price );
                }
            } else {
                window.alert( 'Directions request failed due to ' + status );
            }
        } );
    }
}