declare const VERSION: string;
declare const SERVER_API_URL: string;
declare const DEVELOPMENT: string;

declare module '*.json' {
  const value: any;
  export default value;
}

// src/main/webapp/app/config/typings.d.ts

declare namespace GeoJSON {
  interface GeoJSON {
    type: string;
    [key: string]: any;
  }
}

declare module '@maplibre/maplibre-gl-style-spec' {
  export type DataDrivenPropertyValueSpecification<T> = any;
  export type PropertyValueSpecification<T> = any;
  export type ExpressionFilterSpecification = any;

  export interface SourceSpecification {
    type: string;
    data?: GeoJSON.GeoJSON | string;
    // Add other properties as needed
  }
}

declare module 'azure-maps-control' {
  import {
    DataDrivenPropertyValueSpecification,
    PropertyValueSpecification,
    ExpressionFilterSpecification,
  } from '@maplibre/maplibre-gl-style-spec';

  export interface Map {
    // ... properties and methods
  }

  export { DataDrivenPropertyValueSpecification, PropertyValueSpecification, ExpressionFilterSpecification };

  // ... other types and interfaces you're using
}
