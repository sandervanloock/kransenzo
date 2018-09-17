import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ORDER_DELIVERY_ORIGIN, PRICE_PER_KILOMETER_PER_KM } from '../../app.constants';
import { ICustomer } from 'app/shared/model/customer.model';

declare var google: any;
declare var $: any;

@Component({
    selector: 'jhi-customer-address',
    templateUrl: './customer-address.component.html'
})
export class CustomerAddressComponent implements OnInit {
    @Input() customer: ICustomer;
    @Output() updateDeliveryPrice: EventEmitter<any> = new EventEmitter();

    private searchBox: any;

    constructor() {}

    ngOnInit() {
        const input = document.getElementById('address');
        const options = { componentRestrictions: { country: 'be' } };
        this.searchBox = new google.maps.places.Autocomplete(input, options);
        this.searchBox.addListener('place_changed', () => this.updateAddress());
        google.maps.event.addDomListener(input, 'keydown', function(event) {
            if (event.keyCode === 13) {
                event.preventDefault();
            }
        });
    }

    private updateAddress() {
        const place = this.searchBox.getPlace();
        if (place != null) {
            // as defined in https://developers.google.com/maps/documentation/geocoding/intro#ReverseGeocoding
            this.customer.street = place.address_components
                .filter(attr => attr.types.filter(type => type === 'street_number' || type === 'route').length > 0)
                .map(attr => attr.long_name)
                .reverse()
                .join(' ');
            this.customer.city = place.address_components
                .filter(attr => attr.types.filter(type => type === 'locality').length > 0)
                .map(attr => attr.long_name)
                .join();
            this.customer.province = place.address_components
                .filter(attr => attr.types.filter(type => type === 'administrative_area_level_2').length > 0)
                .map(attr => attr.long_name)
                .join();
            this.customer.zipCode = place.address_components
                .filter(attr => attr.types.filter(type => type === 'postal_code').length > 0)
                .map(attr => attr.long_name)
                .join();
            this.customer.description = place.formatted_address;
            if (place.geometry && place.geometry.location) {
                this.setLocationAndDeliveryPrice(place);
            }
        } else {
            this.customer.street = null;
            this.customer.city = null;
            this.customer.province = null;
            this.customer.zipCode = null;
            this.customer.description = null;
        }
    }

    private setLocationAndDeliveryPrice(place: any) {
        this.customer.latitude = place.geometry.location.lat();
        this.customer.longitude = place.geometry.location.lng();
        const directionsService = new google.maps.DirectionsService();
        directionsService.route(
            {
                origin: ORDER_DELIVERY_ORIGIN,
                destination: { lat: this.customer.latitude, lng: this.customer.longitude },
                waypoints: [],
                optimizeWaypoints: true,
                travelMode: 'DRIVING'
            },
            (response, status) => {
                if (status === 'OK') {
                    if (response.routes.length && response.routes[0].legs.length) {
                        const distance = response.routes[0].legs[0].distance.value;
                        const distanceWithDiscount = Math.max(0, distance - 10000);
                        const price = Math.round(distanceWithDiscount / 1000 * PRICE_PER_KILOMETER_PER_KM * 100) / 100;
                        this.updateDeliveryPrice.emit(price);
                    }
                } else {
                    console.error('Directions request failed due to ' + status);
                }
            }
        );
    }
}
