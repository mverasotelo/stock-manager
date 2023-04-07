import { IAlert } from 'app/shared/model/alert.model';
import { IStock } from 'app/shared/model/stock.model';
import { IAction } from 'app/shared/model/action.model';
import { StoreType } from 'app/shared/model/enumerations/store-type.model';

export interface IStore {
  id?: number;
  code?: string | null;
  type?: StoreType | null;
  alerts?: IAlert[] | null;
  stocks?: IStock[] | null;
  actions?: IAction[] | null;
}

export const defaultValue: Readonly<IStore> = {};
