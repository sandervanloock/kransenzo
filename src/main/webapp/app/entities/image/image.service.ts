import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';

import {Image} from './image.model';
import {createRequestOption, ResponseWrapper} from '../../shared';

@Injectable()
export class ImageService {

    private resourceUrl = 'api/images';
    private resourceSearchUrl = 'api/_search/images';

    constructor( private http: Http ) {
    }

    create( image: Image ): Observable<Image> {
        const copy = this.convert( image );
        return this.http.post( this.resourceUrl, copy ).map( ( res: Response ) => {
            return res.json();
        } );
    }

    update( image: Image ): Observable<Image> {
        const copy = this.convert( image );
        return this.http.put( this.resourceUrl, copy ).map( ( res: Response ) => {
            return res.json();
        } );
    }

    find( id: number ): Observable<Image> {
        return this.http.get( `${this.resourceUrl}/${id}` ).map( ( res: Response ) => {
            return res.json();
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
        return new ResponseWrapper( res.headers, jsonResponse, res.status );
    }

    private convert( image: Image ): Image {
        const copy: Image = Object.assign( {}, image );
        return copy;
    }
}
