export class AddOnGetDTO {
  private _id: number;
  private _name: string;
  private _providesGroups: string;
  private _requiresGroups: string;

  constructor(
    id: number,
    name: string,
    providesgroups: string,
    requiresgroups: string,
  ) {
    this._id = id;
    this._name = name;
    this._providesGroups = providesGroups;
    this._requiresGroups = requiresGroups;
  }

  public get id(): number {
    return this._id;
  }

  public set id(value: number) {
    this._id = value;
  }

  public get name(): string {
    return this._name;
  }

  public set name(value: string) {
    this._name = value;
  }

  public get providesGroups(): string {
    return this._providesGroups;
  }

  public set providesGroups(value: string) {
    this._providesGroups = value;
  }

  public get requiresGroups(): string {
    return this._requiresGroups;
  }

  public set requiresGroups(value: string) {
    this._requiresGroups = value;
  }

  //#(addongetdto-check.model.fragment)#

  public toJSON() {
    return {
      id: _id,
      name: _name,
      providesgroups: _providesgroups,
      requiresgroups: _requiresgroups,
    };
  }
}
