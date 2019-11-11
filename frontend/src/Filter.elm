module Filter exposing
  ( Model
  , Msg
  , init
  , fromQueryString
  , toQueryParameters
  , toForm
  , update
  )

import Bootstrap.Form
import Bootstrap.Form.Checkbox
import Bootstrap.Form.Input
import Bootstrap.Form.Textarea
import Bootstrap.Grid.Col
import Html exposing (..)
import Html.Events
import Json.Decode
import Url.Builder
import Url.Parser.Query



type alias Model =
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


init : Model
init =
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


type Msg
  = MusicTitlesChanged String
  | DiffsBasicChecked Bool
  | DiffsAdvancedChecked Bool
  | DiffsExtremeChecked Bool
  | ModesNormalChecked Bool
  | ModesHardChecked Bool
  | PlayerNamesChanged String
  | RivalIdsChanged String


fromQueryString : Url.Parser.Query.Parser Model
fromQueryString =
  Url.Parser.Query.map Model (Url.Parser.Query.custom "musicTitles" identity)
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


toQueryParameters : Model -> List Url.Builder.QueryParameter
toQueryParameters filter =
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


update : Msg -> Model -> Model
update msg model =
  case msg of
    MusicTitlesChanged musicTitles ->
        { model
        | musicTitles =
            List.filter (not << String.isEmpty) (String.split "\n" musicTitles)
        }

    DiffsBasicChecked bool ->
      let
        diffs = model.diffs
      in
        { model
        | diffs =
            { diffs | basic = bool }
        }

    DiffsAdvancedChecked bool ->
      let
        diffs = model.diffs
      in
        { model
        | diffs =
            { diffs | advanced = bool }
        }

    DiffsExtremeChecked bool ->
      let
        diffs = model.diffs
      in
        { model
        | diffs =
            { diffs | extreme = bool }
        }

    ModesNormalChecked bool ->
      let
        modes = model.modes
      in
        { model
        | modes =
            { modes | normal = bool }
        }

    ModesHardChecked bool ->
      let
        modes = model.modes
      in
        { model
        | modes =
            { modes | hard = bool }
        }

    PlayerNamesChanged playerNames ->
        { model
        | playerNames =
            List.filter (not << String.isEmpty) (String.split "," playerNames)
        }

    RivalIdsChanged rivalIds ->
        { model
        | rivalIds =
            List.filterMap String.toInt (String.split "," rivalIds)
        }


toForm : Model -> Html Msg
toForm filter =
  Bootstrap.Form.form []
    [ Bootstrap.Form.row []
        [ Bootstrap.Form.colLabel [ Bootstrap.Grid.Col.sm2 ] [ text "曲名" ]
        , Bootstrap.Form.col [ Bootstrap.Grid.Col.sm10 ]
            [ Bootstrap.Form.Textarea.textarea
                [ Bootstrap.Form.Textarea.value (String.join "\n" filter.musicTitles)
                , Bootstrap.Form.Textarea.attrs
                    [ Html.Events.on "change" (Json.Decode.map MusicTitlesChanged Html.Events.targetValue) ]
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
                , Bootstrap.Form.Checkbox.onCheck DiffsBasicChecked
                ]
                "BASIC"
            , Bootstrap.Form.Checkbox.checkbox
                [ Bootstrap.Form.Checkbox.inline
                , Bootstrap.Form.Checkbox.id "ADVANCED"
                , Bootstrap.Form.Checkbox.checked filter.diffs.advanced
                , Bootstrap.Form.Checkbox.onCheck DiffsAdvancedChecked
                ]
                "ADVANCED"
            , Bootstrap.Form.Checkbox.checkbox
                [ Bootstrap.Form.Checkbox.inline
                , Bootstrap.Form.Checkbox.id "EXTREME"
                , Bootstrap.Form.Checkbox.checked filter.diffs.extreme
                , Bootstrap.Form.Checkbox.onCheck DiffsExtremeChecked
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
                , Bootstrap.Form.Checkbox.onCheck ModesNormalChecked
                ]
                "NORMAL"
            , Bootstrap.Form.Checkbox.checkbox
                [ Bootstrap.Form.Checkbox.inline
                , Bootstrap.Form.Checkbox.id "HARD"
                , Bootstrap.Form.Checkbox.checked filter.modes.hard
                , Bootstrap.Form.Checkbox.onCheck ModesHardChecked
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
                    [ Html.Events.on "change" (Json.Decode.map PlayerNamesChanged Html.Events.targetValue) ]
                ]
            ]
        ]
    , Bootstrap.Form.row []
        [ Bootstrap.Form.colLabel [ Bootstrap.Grid.Col.sm2 ] [ text "ライバルID" ]
        , Bootstrap.Form.col [ Bootstrap.Grid.Col.sm10 ]
            [ Bootstrap.Form.Input.text
                [ Bootstrap.Form.Input.value (String.join "," (List.map String.fromInt filter.rivalIds))
                , Bootstrap.Form.Input.attrs
                    [ Html.Events.on "change" (Json.Decode.map RivalIdsChanged Html.Events.targetValue) ]
                ]
            ]
        ]
    ]
