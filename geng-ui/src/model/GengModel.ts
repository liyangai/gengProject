export class Geng {
  constructor(obj: Geng) {
    this.id = obj.id;
    this.tagIds = obj.tagIds;
    this.resume = obj.resume;
    this.src = obj.src;
    this.srcType = obj.srcType;
    this.description = obj.description;
  }
  public id: number;
  public resume: string;
  public tagIds: number[];
  public src: string;
  public srcType: string;
  public description: string;
}
