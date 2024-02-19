export type TProduct = Identifiable & Nameable & Descriptionable & {
  content: string,
  contentType: TContentType,
}
export type TContentType = 'image/gif' | 'image/jpg' | 'image/png' | 'text/html'
export type TThemeFolder = Identifiable & Nameable & Descriptionable
export type TTheme = Identifiable & Nameable & Descriptionable

type Identifiable = {
  id: string,
}

type Nameable = {
  name: string,
}

type Descriptionable = {
  description: string,
}
