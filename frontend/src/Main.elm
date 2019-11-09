import Bootstrap.CDN
import Bootstrap.Form
import Bootstrap.Form.Checkbox
import Bootstrap.Form.Input
import Bootstrap.Form.Textarea
import Bootstrap.Grid
import Bootstrap.Grid.Col
import Browser
import Browser.Navigation as Nav
import Html exposing (..)
import Html.Events
import Http
import Json.Decode
import Url
import Url.Builder
import Url.Parser exposing ( (</>), (<?>))
import Url.Parser.Query

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
  = TopRoute Filter


routeParser : Url.Parser.Parser (Route -> a) a
routeParser =
  Url.Parser.oneOf
    [ Url.Parser.map TopRoute (Url.Parser.top <?> queryParser)
    ]


queryParser : Url.Parser.Query.Parser Filter
queryParser =
  Url.Parser.Query.map Filter (Url.Parser.Query.custom "musicTitles" identity)
    |> apply (Url.Parser.Query.custom "diffs" listToFilterDiffs)
    |> apply (Url.Parser.Query.custom "modes" listToFilterModes)
    |> apply (Url.Parser.Query.custom "playerNames" identity)
    |> apply (Url.Parser.Query.custom "rivalIds" (List.filterMap String.toInt))


apply : Url.Parser.Query.Parser a -> Url.Parser.Query.Parser (a -> b) -> Url.Parser.Query.Parser b
apply argParser funcParser =
  Url.Parser.Query.map2 (<|) funcParser argParser


listToFilterDiffs : List String -> FilterDiffs
listToFilterDiffs list =
  { basic = List.member "BASIC" list
  , advanced = List.member "ADVANCED" list
  , extreme = List.member "EXTREME" list
  }


listToFilterModes : List String -> FilterModes
listToFilterModes list =
  { normal = List.member "NORMAL" list
  , hard = List.member "HARD" list
  }


-- MODEL


type alias Model =
  { key : Nav.Key
  , filter : Filter
  , records : List Record.Model
  }

type alias Filter =
  { musicTitles : List String
  , diffs : FilterDiffs
  , modes : FilterModes
  , playerNames : List String
  , rivalIds : List Int
  }

type alias FilterDiffs =
  { basic : Bool
  , advanced : Bool
  , extreme : Bool
  }

type alias FilterModes =
  { normal : Bool
  , hard : Bool
  }


init : () -> Url.Url -> Nav.Key -> (Model, Cmd Msg)
init flags url key =
  ( { key = key
    , filter =
        { musicTitles = []
        , diffs =
            { basic = False
            , advanced = False
            , extreme = False
            }
        , modes =
            { normal = False
            , hard = False
            }
        , playerNames = []
        , rivalIds = []
        }
    , records = []
    }
  , Nav.pushUrl key (Url.toString url)
  )


-- UPDATE


type Msg
  = LinkClicked Browser.UrlRequest
  | UrlChanged Url.Url
  | FilterMusicTitlesChanged String
  | FilterDiffsBasicChecked Bool
  | FilterDiffsAdvancedChecked Bool
  | FilterDiffsExtremeChecked Bool
  | FilterModesNormalChecked Bool
  | FilterModesHardChecked Bool
  | FilterPlayerNamesChanged String
  | FilterRivalIdsChanged String
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
                  Url.Builder.crossOrigin "http://localhost:8080" [ "api", "records" ] (filterToQueryParameters filter)
              , expect =
                  Http.expectJson RecordsGot (Json.Decode.list Record.decoder)
              }
          )

        Nothing ->
          (model, Cmd.none)

    FilterMusicTitlesChanged musicTitles ->
      let
        filter = model.filter
      in
        ( model
        , changeUrl model.key
            { filter
            | musicTitles =
                List.filter (not << String.isEmpty) (String.split "\n" musicTitles)
            }
        )

    FilterDiffsBasicChecked bool ->
      let
        filter = model.filter
        diffs = model.filter.diffs
      in
        ( model
        , changeUrl model.key
            { filter
            | diffs =
                { diffs | basic = bool }
            }
        )

    FilterDiffsAdvancedChecked bool ->
      let
        filter = model.filter
        diffs = model.filter.diffs
      in
        ( model
        , changeUrl model.key
            { filter
            | diffs =
                { diffs | advanced = bool }
            }
        )

    FilterDiffsExtremeChecked bool ->
      let
        filter = model.filter
        diffs = model.filter.diffs
      in
        ( model
        , changeUrl model.key
            { filter
            | diffs =
                { diffs | extreme = bool }
            }
        )

    FilterModesNormalChecked bool ->
      let
        filter = model.filter
        modes = model.filter.modes
      in
        ( model
        , changeUrl model.key
            { filter
            | modes =
                { modes | normal = bool }
            }
        )

    FilterModesHardChecked bool ->
      let
        filter = model.filter
        modes = model.filter.modes
      in
        ( model
        , changeUrl model.key
            { filter
            | modes =
                { modes | hard = bool }
            }
        )

    FilterPlayerNamesChanged playerNames ->
      let
        filter = model.filter
      in
        ( model
        , changeUrl model.key
            { filter
            | playerNames =
                String.split "," playerNames
            }
        )

    FilterRivalIdsChanged rivalIds ->
      let
        filter = model.filter
      in
        ( model
        , changeUrl model.key
            { filter
            | rivalIds =
                List.filterMap String.toInt (String.split "," rivalIds)
            }
        )

    RecordsGot result ->
      case result of
        Ok records ->
          ({ model | records = records }, Cmd.none)

        Err _ ->
          (model, Cmd.none)


changeUrl : Nav.Key -> Filter -> Cmd Msg
changeUrl key filter =
  Nav.pushUrl key (Url.Builder.relative [] (filterToQueryParameters filter))



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
          [ filterToForm model.filter
          , Record.listToTable model.records
          ]
      ]
  }


filterToForm : Filter -> Html Msg
filterToForm filter =
  Bootstrap.Form.form []
    [ Bootstrap.Form.row []
        [ Bootstrap.Form.colLabel [ Bootstrap.Grid.Col.sm2 ] [ text "曲名" ]
        , Bootstrap.Form.col [ Bootstrap.Grid.Col.sm10 ]
            [ Bootstrap.Form.Textarea.textarea
                [ Bootstrap.Form.Textarea.value (String.join "\n" filter.musicTitles)
                , Bootstrap.Form.Textarea.attrs
                    [ Html.Events.on "change" (Json.Decode.map FilterMusicTitlesChanged Html.Events.targetValue) ]
                ]
            ]
        ]
    , Bootstrap.Form.row []
        [ Bootstrap.Form.colLabel [ Bootstrap.Grid.Col.sm2 ] [ text "難易度" ]
        , Bootstrap.Form.col [ Bootstrap.Grid.Col.sm10 ]
            [ Bootstrap.Form.Checkbox.checkbox
                [ Bootstrap.Form.Checkbox.inline
                , Bootstrap.Form.Checkbox.id "BASIC"
                , Bootstrap.Form.Checkbox.checked filter.diffs.basic
                , Bootstrap.Form.Checkbox.onCheck FilterDiffsBasicChecked
                ]
                "BASIC"
            , Bootstrap.Form.Checkbox.checkbox
                [ Bootstrap.Form.Checkbox.inline
                , Bootstrap.Form.Checkbox.id "ADVANCED"
                , Bootstrap.Form.Checkbox.checked filter.diffs.advanced
                , Bootstrap.Form.Checkbox.onCheck FilterDiffsAdvancedChecked
                ]
                "ADVANCED"
            , Bootstrap.Form.Checkbox.checkbox
                [ Bootstrap.Form.Checkbox.inline
                , Bootstrap.Form.Checkbox.id "EXTREME"
                , Bootstrap.Form.Checkbox.checked filter.diffs.extreme
                , Bootstrap.Form.Checkbox.onCheck FilterDiffsExtremeChecked
                ]
                "EXTREME"
            ]
        ]
    , Bootstrap.Form.row []
        [ Bootstrap.Form.colLabel [ Bootstrap.Grid.Col.sm2 ] [ text "ゲームモード" ]
        , Bootstrap.Form.col [ Bootstrap.Grid.Col.sm10 ]
            [ Bootstrap.Form.Checkbox.checkbox
                [ Bootstrap.Form.Checkbox.inline
                , Bootstrap.Form.Checkbox.id "NORMAL"
                , Bootstrap.Form.Checkbox.checked filter.modes.normal
                , Bootstrap.Form.Checkbox.onCheck FilterModesNormalChecked
                ]
                "NORMAL"
            , Bootstrap.Form.Checkbox.checkbox
                [ Bootstrap.Form.Checkbox.inline
                , Bootstrap.Form.Checkbox.id "HARD"
                , Bootstrap.Form.Checkbox.checked filter.modes.hard
                , Bootstrap.Form.Checkbox.onCheck FilterModesHardChecked
                ]
                "HARD"
            ]
        ]
    , Bootstrap.Form.row []
        [ Bootstrap.Form.colLabel [ Bootstrap.Grid.Col.sm2 ] [ text "プレイヤー名" ]
        , Bootstrap.Form.col [ Bootstrap.Grid.Col.sm10 ]
            [ Bootstrap.Form.Input.text
                [ Bootstrap.Form.Input.value (String.join "," filter.playerNames)
                , Bootstrap.Form.Input.attrs
                    [ Html.Events.on "change" (Json.Decode.map FilterPlayerNamesChanged Html.Events.targetValue) ]
                ]
            ]
        ]
    , Bootstrap.Form.row []
        [ Bootstrap.Form.colLabel [ Bootstrap.Grid.Col.sm2 ] [ text "ライバルID" ]
        , Bootstrap.Form.col [ Bootstrap.Grid.Col.sm10 ]
            [ Bootstrap.Form.Input.text
                [ Bootstrap.Form.Input.value (String.join "," (List.map String.fromInt filter.rivalIds))
                , Bootstrap.Form.Input.attrs
                    [ Html.Events.on "change" (Json.Decode.map FilterRivalIdsChanged Html.Events.targetValue) ]
                ]
            ]
        ]
    ]


filterToQueryParameters : Filter -> List Url.Builder.QueryParameter
filterToQueryParameters filter =
  List.concat
    [ List.map (Url.Builder.string "musicTitles") filter.musicTitles
    , List.filterMap identity
        [ if filter.diffs.basic then Just (Url.Builder.string "diffs" "BASIC") else Nothing
        , if filter.diffs.advanced then Just (Url.Builder.string "diffs" "ADVANCED") else Nothing
        , if filter.diffs.extreme then Just (Url.Builder.string "diffs" "EXTREME") else Nothing
        ]
    , List.filterMap identity
        [ if filter.modes.normal then Just (Url.Builder.string "modes" "NORMAL") else Nothing
        , if filter.modes.hard then Just (Url.Builder.string "modes" "HARD") else Nothing
        ]
    , List.map (Url.Builder.string "playerNames") filter.playerNames
    , List.map (Url.Builder.int "rivalIds") filter.rivalIds
    ]
