import { IAlert } from 'app/shared/model/alert.model';
import { IAction } from 'app/shared/model/action.model';
import { IArticle } from 'app/shared/model/article.model';
import { IStore } from 'app/shared/model/store.model';

export interface IStock {
  id?: number;
  actualStock?: number | null;
  reorderPoint?: number | null;
  maxStock?: number | null;
  section?: string | null;
  level?: string | null;
  rack?: string | null;
  alerts?: IAlert[] | null;
  actions?: IAction[] | null;
  article?: IArticle | null;
  store?: IStore | null;
  isUnderReorderPoint?: boolean | null;
}

export const defaultValue: Readonly<IStock> = {};

export interface IStockCriteria {
  store?: number;
  article?: string;
}

export class StockCriteria implements IStockCriteria {
  constructor(
    public store?: number,
    public article?: string,
  ) {}
}
