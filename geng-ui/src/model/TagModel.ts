export class Tag {
  constructor(obj: Tag) {
    this.id = obj.id;
    this.parentId = obj.parentId;
    this.tagName = obj.tagName;
    this.tagIcon = obj.tagIcon;
    this.description = obj.description;
  }
  public id: number;
  public tagName: string;
  public parentId: number | null;
  public tagIcon: string;
  public description: string;
}

export interface TagNode {
  id: number;
  label: string;
  tag: Tag;
  children: TagNode[];
}
