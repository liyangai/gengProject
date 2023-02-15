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

export class TagNode {
  constructor(obj: Tag) {
    this.id = obj.id;
    this.parentId = obj.parentId;
    this.tagName = obj.tagName;
    this.tagIcon = obj.tagIcon;
    this.description = obj.description;
    this.children = [];
  }
  public id: number;
  public tagName: string;
  public parentId: number | null;
  public tagIcon: string;
  public description: string;
  public children: TagNode[];
}

// export interface TagNode {
//   id: number;
//   tagName: string;
//   parentId: number | null;
//   tagIcon: string;
//   description: string;
//   children: TagNode[] | null;
// }

// export interface TagNode {
//   id: number;
//   label: string;
//   tag: Tag;
//   children: TagNode[];
// }
