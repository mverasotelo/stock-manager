import { IStock } from 'app/shared/model/stock.model';
import { ArticleType } from 'app/shared/model/enumerations/article-type.model';

export interface IArticle {
  id?: number;
  code?: string | null;
  description?: string | null;
  type?: ArticleType | null;
  stocks?: IStock[] | null;
}

export const defaultValue: Readonly<IArticle> = {};

export interface IArticleCriteria {
  code?: {};
  description?: {};
}

export class ArticleCriteria implements IArticleCriteria {
  constructor(
    public code?: {},
    public description?: {}
  ) {}
}