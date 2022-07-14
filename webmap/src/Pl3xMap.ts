import {Control, latLng, LatLng, Map, point, Point, TileLayer} from "leaflet";
import {CoordsControl} from "./control/CoordsControl";
import {LinkControl} from "./control/LinkControl";
import {PlayerLayerGroup} from "./layergroup/PlayerLayerGroup";
import {World} from "./module/World";
import {Lang} from "./options/Lang";
import {Options} from "./options/Options";
import {JSON, RootJSON} from "./types/Json";

import "./scss/styles.scss";
import Pl3xmapLeafletMap from "./map/Pl3xmapLeafletMap";
import LayersControl from "./control/LayersControl";

window.onload = function () {
    new Pl3xMap();
};

export class Pl3xMap {
    private readonly _map: Map;
    private _options: Options = new Options();
    private _lang: Lang = new Lang();
    private _world: World | null = null;
    private _tileLayer: TileLayer | null = null;
    private _playersLayer: PlayerLayerGroup | null = null;
    private _layerControls: Control.Layers | null = null;
    private _coordsControl: CoordsControl | null = null;
    private _linkControl: LinkControl | null = null;

    constructor() {
        this._map = new Pl3xmapLeafletMap();

        this.getJSON('tiles/settings.json', (json: RootJSON) => this.init(json));
    }

    init(json: RootJSON): void {
        document.title = json.ui.lang.title;

        this._lang.coordsLabel = json.ui.lang.coords.label;
        this._lang.coordsValue = json.ui.lang.coords.value;
        this._lang.players = json.ui.lang.players;
        this._lang.worlds = json.ui.lang.worlds;

        this._options.ui.link = json.ui.link;
        this._options.ui.coords = json.ui.coords;

        this._options.format = json.format;

        // load world from url, or first world from json
        this._world = new World(this, this.getUrlParam('world', json.worlds[0]?.name ?? 'world'));

        // player tracker layer
        this._playersLayer = new PlayerLayerGroup().setZIndex(100).addTo(this._map);

        // set up layer controls
        this._layerControls = new LayersControl({}, {}, {position: 'topleft'})
            .addOverlay(this._playersLayer, 'Players')
            .addTo(this._map);

        // add the coords ui control box
        if (this._options.ui.coords) {
            this._coordsControl = new CoordsControl(this).addTo(this._map);
        }

        // add the link ui control box
        if (this._options.ui.link) {
            this._linkControl = new LinkControl(this).addTo(this._map);
        }
    }

    toLatLng(x: number, z: number): LatLng {
        return latLng(this.pixelsToMeters(z), this.pixelsToMeters(x));
    }

    toPoint(latlng: LatLng): Point {
        return point(this.metersToPixels(latlng.lng), this.metersToPixels(latlng.lat));
    }

    pixelsToMeters(num: number): number {
        return num * this.scale();
    }

    metersToPixels(num: number): number {
        return num / this.scale();
    }

    scale(): number {
        return 1 / Math.pow(2, this.getMaxZoom());
    }

    getMaxZoom(): number {
        return this._world?.zoom.maxOut ?? 0;
    }

    centerOn(x: number, z: number, zoom: number) {
        this._map.setView(this.toLatLng(x, z), this.getMaxZoom() - zoom);
        return this._map;
    }

    getUrlParam<T>(query: string, def: T): T {
        return new URLSearchParams(window.location.search).get(query) as unknown as T ?? def;
    }

    getUrlFromView(): string {
        const center: Point = this.toPoint(this._map.getCenter());
        const zoom: number = this.getMaxZoom() - this._map.getZoom();
        const x: number = Math.floor(center.x);
        const z: number = Math.floor(center.y);
        const world: string = this._world?.name ?? '';
        const type: string = this._world?.renderer ?? '';
        return `?world=${world}&renderer=${type}&zoom=${zoom}&x=${x}&z=${z}`;
    }

    getJSON(url: string, fn: (json: JSON) => void) {
        fetch(url, {cache: "no-store"})
            .then(async res => {
                if (res.ok) {
                    fn(await res.json());
                }
            });
    }

    get map(): Map {
        return this._map
    }

    get options(): Options {
        return this._options;
    }

    get lang(): Lang {
        return this._lang;
    }

    get world(): World | null {
        return this._world;
    }

    get tileLayer(): TileLayer | null {
        return this._tileLayer;
    }

    set tileLayer(tileLayer: TileLayer | null) {
        this._tileLayer = tileLayer;
    }

    get playersLayer(): PlayerLayerGroup | null {
        return this._playersLayer;
    }

    get layerControls(): Control.Layers | null {
        return this._layerControls;
    }

    get coordsControl(): CoordsControl | null {
        return this._coordsControl;
    }

    get linkControl(): LinkControl | null {
        return this._linkControl;
    }
}
