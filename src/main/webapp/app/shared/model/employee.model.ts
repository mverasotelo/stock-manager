import { IAction } from 'app/shared/model/action.model';

export interface IEmployee {
  id?: number;
  codigo?: number | null;
  name?: string | null;
  actions?: IAction[] | null;
}

export const defaultValue: Readonly<IEmployee> = {};
