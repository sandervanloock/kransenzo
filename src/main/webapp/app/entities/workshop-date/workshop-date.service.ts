import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {SERVER_API_URL} from '../../app.constants';

import {JhiDateUtils} from 'ng-jhipster';

import {WorkshopDate} from './workshop-date.model';
import {createRequestOption, ResponseWrapper} from '../../shared';

@Injectable()
export class WorkshopDateService {

    private resourceUrl = SERVER_API_URL + 'api/workshop-dates';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/workshop-dates';

    constructor( private http: Http, private dateUtils: JhiDateUtils ) {
    }

    create( workshopDate: WorkshopDate ): Observable<WorkshopDate> {
        const copy = this.convert( workshopDate );
        return this.http.post( this.resourceUrl, copy ).map( ( res: Response ) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer( jsonResponse );
        } );
    }

    update( workshopDate: WorkshopDate ): Observable<WorkshopDate> {
        const copy = this.convert( workshopDate );
        return this.http.put( this.resourceUrl, copy ).map( ( res: Response ) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer( jsonResponse );
        } );
    }

    find( id: number ): Observable<WorkshopDate> {
        return this.http.get( `${this.resourceUrl}/${id}` ).map( ( res: Response ) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer( jsonResponse );
        } );
    }

    query( req?: any ): Observable<ResponseWrapper> {
        const options = createRequestOption( req );
        return this.http.get( this.resourceUrl, options )
            .map( ( res: Response ) => this.convertResponse( res ) );
    }

    delete( id: number ): Observable<Response> {
        return this.http.delete( `${this.resourceUrl}/${id}` );
    }

    search( req?: any ): Observable<ResponseWrapper> {
        const options = createRequestOption( req );
        return this.http.get( this.resourceSearchUrl, options )
            .map( ( res: any ) => this.convertResponse( res ) );
    }

    private convertResponse( res: Response ): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for ( let i = 0; i < jsonResponse.length; i++ ) {
            result.push( this.convertItemFromServer( jsonResponse[i] ) );
        }
        return new ResponseWrapper( res.headers, result, res.status );
    }

    /**
     * Convert a returned JSON object to WorkshopDate.
     */
    private convertItemFromServer( json: any ): WorkshopDate {
        const entity: WorkshopDate = Object.assign( new WorkshopDate(), json );
        entity.date = this.dateUtils
            .convertDateTimeFromServer( json.date );
        return entity;
    }

    /**
     * Convert a WorkshopDate to a JSON which can be sent to the server.
     */
    private convert( workshopDate: WorkshopDate ): WorkshopDate {
        const copy: WorkshopDate = Object.assign( {}, workshopDate );

        copy.date = this.dateUtils.toDate( workshopDate.date );
        return copy;
    }
}
