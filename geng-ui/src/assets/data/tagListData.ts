import { TagTypeConstant } from "@/common/constant/TagType";
import { Tag } from "@/model/TagModel";

export const mockTagList: Tag[] = [
  {
    id: 1,
    tagName: "tag1",
    description: "tag1",
    parentId: null,
    tagIcon: TagTypeConstant.COMMON,
  },
  {
    id: 2,
    tagName: "tag2",
    description: "tag2",
    parentId: 1,
    tagIcon: TagTypeConstant.COMMON,
  },
  {
    id: 3,
    tagName: "tag3",
    description: "tag3",
    parentId: 1,
    tagIcon: TagTypeConstant.COMMON,
  },
  {
    id: 4,
    tagName: "tag4",
    description: "tag4",
    parentId: null,
    tagIcon: TagTypeConstant.COMMON,
  },
  {
    id: 5,
    tagName: "tag5",
    description: "tag5",
    parentId: 4,
    tagIcon: TagTypeConstant.COMMON,
  },
  {
    id: 6,
    tagName: "tag6",
    description: "tag6",
    parentId: 4,
    tagIcon: TagTypeConstant.COMMON,
  },
];
