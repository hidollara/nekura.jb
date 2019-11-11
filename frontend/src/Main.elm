module Main exposing (..)

import Bootstrap.CDN
import Bootstrap.Grid
import Browser
import Browser.Navigation as Nav
import Html
import Http
import Json.Decode
import Url
import Url.Builder
import Url.Parser exposing ( (</>), (<?>))

import Filter
import Record



-- MAIN


main : Program () Model Msg
main =
  Browser.application
    { init = init
    , view = view
    , update = update
    , subscriptions = subscriptions
    , onUrlChange = UrlChanged
    , onUrlRequest = LinkClicked
    }



-- ROUTER


type Route
  = TopRoute Filter.Model


routeParser : Url.Parser.Parser (Route -> a) a
routeParser =
  Url.Parser.oneOf
    [ Url.Parser.map TopRoute (Url.Parser.top <?> Filter.fromQueryString)
    ]



-- MODEL


type alias Model =
  { key : Nav.Key
  , filter : Filter.Model
  , records : List Record.Model
  }


init : () -> Url.Url -> Nav.Key -> (Model, Cmd Msg)
init flags url key =
  ( { key = key
    , filter = Filter.init
    , records = []
    }
  , Nav.pushUrl key (Url.toString url)
  )



-- UPDATE


type Msg
  = LinkClicked Browser.UrlRequest
  | UrlChanged Url.Url
  | FilterMsgGot Filter.Msg
  | RecordsGot (Result Http.Error (List Record.Model))


update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
  case msg of
    LinkClicked urlRequest ->
      case urlRequest of
        Browser.Internal url ->
          (model, Nav.pushUrl model.key (Url.toString url))

        Browser.External href ->
          (model, Nav.load href)

    UrlChanged url ->
      case (Url.Parser.parse routeParser url) of
        Just (TopRoute filter) ->
          ( { model | filter = filter }
          , Http.get
              { url =
                  Url.Builder.crossOrigin "http://localhost:8080" [ "api", "records" ] (Filter.toQueryParameters filter)
              , expect =
                  Http.expectJson RecordsGot (Json.Decode.list Record.decoder)
              }
          )

        Nothing ->
          (model, Cmd.none)

    FilterMsgGot filterMessage ->
      let
        filter = Filter.update filterMessage model.filter
      in
        ({ model | filter = filter }, changeUrl model.key filter)

    RecordsGot result ->
      case result of
        Ok records ->
          ({ model | records = records }, Cmd.none)

        Err _ ->
          (model, Cmd.none)


changeUrl : Nav.Key -> Filter.Model -> Cmd Msg
changeUrl key filter =
  Nav.pushUrl key (Url.Builder.relative ["."] (Filter.toQueryParameters filter))



-- SUBSCRIPTIONS


subscriptions : Model -> Sub Msg
subscriptions _ = Sub.none



-- VIEW


view : Model -> Browser.Document Msg
view model =
  { title = "根暗.jb"
  , body =
        [ Bootstrap.CDN.stylesheet
        , Bootstrap.Grid.container []
            [ Html.map FilterMsgGot (Filter.toForm model.filter)
            , Record.listToTable model.records
            ]
        ]
  }
