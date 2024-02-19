const reorderList = <T>(list: T[], onReorder: (list: T[]) => void, from: number, to: number) => {
  const [moved] = list.splice(from, 1);
  list.splice(to, 0, moved);
  onReorder(list);
};

export { reorderList };
